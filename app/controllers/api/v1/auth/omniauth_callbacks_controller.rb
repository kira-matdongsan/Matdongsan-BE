class Api::V1::Auth::OmniauthCallbacksController < Devise::OmniauthCallbacksController
  include ApiResponse
  include ExceptionHandler
  respond_to :json

  def kakao
    # TODO: 카카오 로그인 시 제공받은 닉네임, 프로필 이미지로 프로필 데이터 추가 필요
    handle_oauth('Kakao')
  end

  private

  def handle_oauth(provider)
    auth_data = request.env["omniauth.auth"]
    user = User.from_omniauth(auth_data)

    if user.persisted?
      oauth_token = OauthToken.find_or_initialize_by(
        user: user,
        provider: provider.downcase
      )

      oauth_token.update!(
        access_token: auth_data.credentials.token,
        refresh_token: auth_data.credentials.refresh_token,
        expires_at: Time.at(auth_data.credentials.expires_at).to_datetime
      )

      device_info = request.user_agent
      access_token, refresh_token = Auth::TokenService.issue_tokens(user, device_info)

      render_json :success, data: { token: { access_token: access_token, refresh_token: refresh_token }, user: user.as_json(only: [:id, :email]) }
    else
      render json: { error: "Failed to authenticate with #{provider}" }, status: :unprocessable_entity
    end
  end
end
